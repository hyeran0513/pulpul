package dongduk.cs.pulpul.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dongduk.cs.pulpul.controller.FileCommand;
import dongduk.cs.pulpul.dao.BorrowDao;
import dongduk.cs.pulpul.dao.ItemDao;
import dongduk.cs.pulpul.dao.MemberDao;
import dongduk.cs.pulpul.domain.Goods;
import dongduk.cs.pulpul.domain.ShareThing;
import dongduk.cs.pulpul.service.exception.DeleteItemException;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemDao itemDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private BorrowDao borrowDao;
	
	@Transactional
	@Override
	public void deleteItem(String itemId, String memberId, String uploadDir) throws DeleteItemException {	
		if (itemId.substring(0, 1).equals("G") && itemDao.isExistOrder(itemId)) { // 품목이 상품이면 주문내역 존재여부 확인
			System.out.println("주문내역 있음");
			throw new DeleteItemException("주문내역이 존재하여 삭제할 수 없습니다. 비공개로 돌려주시기 바랍니다.");
		}
		else if (itemId.substring(0, 1).equals("S") && itemDao.isExistBorrow(itemId)) { // 품목이 공유물품이면 대여내역 존재여부 확인
			System.out.println("대여내역 있음");
			throw new DeleteItemException("대여내역이 존재하여 삭제할 수 없습니다. 비공개로 돌려주시기 바랍니다.");
		}

		itemDao.deleteItem(itemId);	// 품목 삭제
		int cnt = itemDao.deleteItemImages(itemId, memberId);	// 품목 이미지 레코드 삭제
		deleteFileByItem(itemId, uploadDir, cnt); // 품목 이미지 삭제
	}

	@Override
	public List<Goods> getGoodsList() {
		return itemDao.findAllGoods();
	}

	@Override
	public List<Goods> getGoodsListByMember(String memberId) {
		return itemDao.findGoodsByMember(memberId);
	}

	@Override
	public List<Goods> getGoodsListByMarket(int marketId) {
		return itemDao.findGoodsByMarket(marketId);
	}

	@Override
	public List<Goods> getNewGoodsList() {
		return itemDao.findNewGoods();
	}

	@Override
	public Goods getGoods(String itemId) {
		return itemDao.findGoodsByItem(itemId);
	}

	@Transactional
	@Override
	public void uploadGoods(Goods goods, FileCommand uploadFiles) {
		itemDao.createGoods(goods);	// 상품 레코드 생성
		
		String itemId = goods.getItem().getId();
		if (itemId != null) {
			goods.getItem().setId(itemId);			
			List<String> imageUrlList = new ArrayList<String>();
			int cnt = 1;
			for (MultipartFile uploadFile : uploadFiles.getFiles()) {
				if (!uploadFile.isEmpty()) {
					uploadFiles.setFile(uploadFile);
					String filename = uploadFile(uploadFiles, goods.getItem().getId(), cnt);	// 상품 이미지 파일 저장
					imageUrlList.add("/upload/" + filename);
					cnt++;
				}
			}		
			String memberId = goods.getItem().getMarket().getMember().getId();
			itemDao.createItemImages(imageUrlList, memberId);	// 상품 이미지 레코드 생성		
			memberDao.changePoint(memberId, 1, 500);	// 포인트 +500
		}
	}

	@Transactional
	@Override
	public void changeGoodsInfo(Goods goods, FileCommand updateFiles, String[] deleteImages) {
		itemDao.changeItemInfo(goods.getItem());	// 품목 레코드 수정
		itemDao.changeGoodsInfo(goods);	// 상품 레코드 수정
		
		String itemId = goods.getItem().getId();
		String memberId = goods.getItem().getMarket().getMember().getId();
		itemDao.deleteItemImages(itemId, memberId); // 상품 이미지 레코드 삭제
		if (deleteImages != null)
			deleteFileByImages(updateFiles.getPath(), deleteImages);	// 상품 이미지 파일 삭제
		
		List<String> imageUrlList = updateFileByItem(updateFiles.getPath(), itemId);	// 기존에 있던 상품 이미지 파일명 변경
		int newCnt = imageUrlList.size() + 1;
		for (MultipartFile updateFile : updateFiles.getFiles()) {
			if (!updateFile.isEmpty()) {
				updateFiles.setFile(updateFile);
				String filename = uploadFile(updateFiles, itemId, newCnt);	// 새로운 상품 이미지 파일 저장
				imageUrlList.add("/upload/" + filename);
				newCnt++;
			}
		}
		itemDao.createItemImages(imageUrlList, memberId); // 새로운 상품 이미지 레코드 생성
	}

	@Override
	public List<ShareThing> getShareThingList() {
		List<ShareThing> shareThingList = itemDao.findAllShareThing();		
		if (shareThingList != null) {
			for (ShareThing shareThing : shareThingList) {
				String itemId = shareThing.getItem().getId();
				shareThing.setReservationNumber(borrowDao.checkNumberBorrowReservation(itemId));	// 공유물품 예약자 수 조회
				
				String[] addressSplit = shareThing.getItem().getMarket().getMember().getAddress().split(" ");
				shareThing.getItem().getMarket().getMember().setAddress(addressSplit[0] + " " + addressSplit[1]);
			}
		}
		return shareThingList;
	}
	
	@Override
	public List<ShareThing> getShareThingListByMember(String memberId) {
		List<ShareThing> shareThingList = itemDao.findShareThingByMember(memberId);
		if (shareThingList != null) {
			for (ShareThing shareThing : shareThingList) {
				String itemId = shareThing.getItem().getId();
				shareThing.setReservationNumber(borrowDao.checkNumberBorrowReservation(itemId));
				
				String[] addressSplit = shareThing.getItem().getMarket().getMember().getAddress().split(" ");
				String addressInfo = addressSplit[0] + " " + addressSplit[1];
				shareThing.getItem().getMarket().getMember().setAddress(addressInfo);
			}
		}
		return shareThingList;
	}

	@Override
	public List<ShareThing> getShareThingListByMarket(int marketId) {
		List<ShareThing> shareThingList = itemDao.findShareThingByMarket(marketId);
		if (shareThingList != null) {
			for (ShareThing shareThing : shareThingList) {
				String itemId = shareThing.getItem().getId();
				shareThing.setReservationNumber(borrowDao.checkNumberBorrowReservation(itemId));
			}
		}
		return shareThingList;
	}

	@Override
	public ShareThing getShareThing(String itemId) {
		ShareThing shareThing = itemDao.findShareThingByItem(itemId);
		if (shareThing != null) {
			shareThing.setReservationNumber(borrowDao.checkNumberBorrowReservation(itemId));
			
			String[] addressSplit = shareThing.getItem().getMarket().getMember().getAddress().split(" ");
			String addressInfo = addressSplit[0] + " " + addressSplit[1];
			shareThing.getItem().getMarket().getMember().setAddress(addressInfo);
		}
		return shareThing;
	}
	
	@Transactional
	@Override
	public void uploadShareThing(ShareThing shareThing, FileCommand uploadFiles) {	
		itemDao.createShareThing(shareThing);	// 공유물품 레코드 생성
		
		String itemId = shareThing.getItem().getId();
		if (itemId != null) {
			shareThing.getItem().setId(itemId);
			List<String> imageUrlList = new ArrayList<String>();
			int cnt = 1;
			for (MultipartFile uploadFile : uploadFiles.getFiles()) {
				if (!uploadFile.isEmpty()) {
					uploadFiles.setFile(uploadFile);
					String filename = uploadFile(uploadFiles, shareThing.getItem().getId(), cnt);	// 공유물품 이미지 파일 저장
					imageUrlList.add("/upload/" + filename);
					cnt++;
				}
			}
			String memberId = shareThing.getItem().getMarket().getMember().getId();
			System.out.println(memberId);
			itemDao.createItemImages(imageUrlList, memberId);	// 공유물품 이미지 레코드 생성
		}
	}

	@Transactional
	@Override
	public void changeShareThingInfo(ShareThing shareThing, FileCommand updateFiles, String[] deleteImages) {	
		itemDao.changeItemInfo(shareThing.getItem());	// 품목 레코드 수정
		itemDao.changeShareThingInfo(shareThing);	// 공유물품 레코드 수정	
		
		String itemId = shareThing.getItem().getId();
		String memberId = shareThing.getItem().getMarket().getMember().getId();
		itemDao.deleteItemImages(itemId, memberId);	// 공유물품 이미지 레코드 삭제
		if (deleteImages != null)
			deleteFileByImages(updateFiles.getPath(), deleteImages);	// 공유물품 이미지 파일 삭제
		
		List<String> imageUrlList = updateFileByItem(updateFiles.getPath(), itemId);	// 기존에 있던 공유물품 이미지 파일명 변경
		int newCnt = imageUrlList.size() + 1;
		for (MultipartFile updateFile : updateFiles.getFiles()) {
			if (!updateFile.isEmpty()) {
				updateFiles.setFile(updateFile);
				String filename = uploadFile(updateFiles, itemId, newCnt);	// 새로운 공유물품 이미지 파일 저장
				imageUrlList.add("/upload/" + filename);
				newCnt++;
			}
		}
		itemDao.createItemImages(imageUrlList, memberId); // 새로운 공유물품 이미지 레코드 생성
	}
	
	// 이미지 파일 업로드 메소드
	public String uploadFile(FileCommand uploadFiles, String itemId, int cnt) {
		String newFilename = "";
		try {
            newFilename = itemId + "-" + cnt + ".jpg";
            
            File newFile = new File(uploadFiles.getPath(), newFilename);
            if (newFile.exists())
            	newFile.delete();
            uploadFiles.getFile().transferTo(newFile);

        }catch(Exception e) {            
            e.printStackTrace();
        }
		return newFilename;
	}
	
	// 특정 상품에 대한 이미지 파일 이름 변경 메소드
	public ArrayList<String> updateFileByItem(String path, String itemId) {	
		ArrayList<String> imageUrlList = new ArrayList<String>();
		try {
			File f = new File(path);

            if(f.isDirectory()) {
                File[] fList = f.listFiles();
                int cnt = 1;
                for(int i = 0; i < fList.length; i++) {
                	String filename = fList[i].getName();
                	if (filename.contains(itemId)) {
                		String newFilename = itemId + "-" + cnt + ".jpg";
                		File newFile = new File(path, newFilename);
                		fList[i].renameTo(newFile);
                		imageUrlList.add("/upload/" + newFilename);
                		cnt++;
                	}
                }
            }
        }catch(Exception e) {            
            e.printStackTrace();
        }	
		return imageUrlList;
	}
	
	// 특정 이미지와 동일한 이름의 이미지 파일 삭제 메소드
	public void deleteFileByImages(String uploadDir, String[] deleteImages) {	
		try {       
			for (String deleteImage : deleteImages) {
				File file = new File(uploadDir, deleteImage.substring(8));
				if (file.exists())
					file.delete();
			}
		}catch(Exception e) {            
			e.printStackTrace();
		}
	}
	
	// 특정 상품에 대한 모든 이미지 파일 삭제 메소드
	public void deleteFileByItem(String itemId, String uploadDir, int cnt) {	
		try {       
			for (int i = 1; i <= cnt; i++) {
				String filename = itemId + "-" + i + ".jpg"; 
				File file = new File(uploadDir, filename);
				if (file.exists())
					file.delete();
			}
		}catch(Exception e) {            
			e.printStackTrace();
		}
	}

}
