 /*
* input file에서 이미지 선택 시 선택한 이미지를 보여줌.
  goodsForm, shareThingForm
*/
let fileArray; //fileLits 담을 변수
let addFileArray;
let check = 0;

  function previewFiles() {

  let files = document.querySelector('input[type=file]').files;
  if(files.length == 0) return false;
  	
	//처음 이미지 삽입할 때
	if(check == 0){
		 files = document.querySelector('input[type=file]').files; 
		 fileArray = Array.from(files); //변수에 할당된 파일을 배열로 변환(FileList -> Array) -> filsList는 js에서 보안상(?) 수정할 수 없음. 
		
		 check = 1;
	}
	
	//사용자가 여러번 이미지 삽입할 때
	else{
		//fileArray는 처음 추가한 이미지 파일들
		//addFileArray 이후에 더 추가한 이미지 파일들
		let addFileArray = Array.from(files);
		console.log(addFileArray);
		console.log(fileArray);
		for(var i = 0; i < fileArray.length; i++){
			for(var j = 0; j < addFileArray.length; j++){
				if(fileArray[i].name == addFileArray[j].name){
					Swal.fire({
				   text: '같은 이름의 이미지가 존재합니다.',
				   confirmButtonColor: '#93c0b5',
				   confirmButtonText: '확인',
				 });
					return false;
				}
		 }
		}
		
		
		for(var i = 0; i < addFileArray.length; i++){
			fileArray.push(addFileArray[i]);
		}
		console.log(fileArray);
	}
	
	//선택한 이미지 보여주기
    function readAndPreview(file) {
     
      var reader = new FileReader();

      reader.addEventListener("load", function () {
		
		const div = document.querySelector("#putItemImg");
		const span = document.createElement("span"); //<span></span>
		span.classList.add("putItemImgSpan");
		
		//<img></img>
        let image = new Image();
        image.height = 100;
        image.width = 100;
        image.title = file.name;
        image.src = this.result;
        span.appendChild(image);
        div.appendChild(span);
        
         //삭제 버튼 <button></button>
        const button = document.createElement("button");
        
        button.innerText = "X";
        button.classList.add("deleteBtn");
        button.id = image.title; //id를 filename으로
        button.type = "button";
        span.appendChild(button);
        
        // 이미지 삭제 버튼 눌렀을 때
        button.addEventListener("click", deleteImg);
	       

	 }, false);
 
    reader.readAsDataURL(file);
 	}
 	if (files) {
		    [].forEach.call(files, readAndPreview);
   }
 }
 
 //기존에 존재하던 이미지 수정
 function updateImg(event){
	
	event.preventDefault();
	let selectdeleteBtn = event.target.id;//선택한 button의 아이디
	
	console.log(selectdeleteBtn);
	let fileDiv = document.querySelector('.putFilenameDiv');
	let deleteFilename = document.createElement('input');
	
	//input type=hidden으로 기존에 있는데 삭제된 파일 controller에 보내기
	deleteFilename.setAttribute("type", "hidden");
	deleteFilename.setAttribute("name", "deleteImages");
	deleteFilename.setAttribute("value", selectdeleteBtn);
	
	fileDiv.appendChild(deleteFilename);
	

	event.target.parentElement.remove();

}
 function deleteImg(event){
	     event.preventDefault();
	       
	       let deleteFile = event.target.id;//선택한 button의 아이디
	       
	       for(var i = 0; i < fileArray.length; i++){
				if(deleteFile == fileArray[i].name){
					fileArray.splice(i, 1);//해당하는 index의 파일을 배열에서 제거 
					break;
				}
			}
			
		  if(fileArray.length == 0){
			check = 0;
		  }
		  /*<span> 버튼의 부모 element span 삭제
		       <button ..></button>
		    </span>
		  */
		  event.target.parentElement.remove(); 
		 
		 //남은 배열을 dataTransfer로 처리(Array -> FileList) 
	     const dataTransfer = new DataTransfer();
         fileArray.forEach(file => { dataTransfer.items.add(file); });
         
         document.querySelector('input[type=file]').files = dataTransfer.files;
  		}