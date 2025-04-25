document.addEventListener('DOMContentLoaded', () => {
	// — 썸네일 미리보기 & 삭제 처리 —
	const thumbInput = document.getElementById('thumbnail');
	const thumbPreview = document.getElementById('thumbnailPreview');

	thumbInput.addEventListener('change', () => {
		thumbPreview.innerHTML = '';
		const file = thumbInput.files[0];
		if (!file) return;

		const wrapper = document.createElement('div');
		wrapper.classList.add('preview-item');
		wrapper.innerHTML = `
			<img src="${URL.createObjectURL(file)}" alt="${file.name}" />
			<button type="button" class="btn-remove">&times;</button>
			`;
		thumbPreview.append(wrapper);

		wrapper.querySelector('.btn-remove')
			.addEventListener('click', () => {
				thumbInput.value = '';   // 선택 초기화
				wrapper.remove();        // 미리보기 제거
			});
	});

	// — 추가 이미지 (최대 4장) 미리보기 & 삭제 처리 —
	const imgInput = document.getElementById('images');
	const imgPreview = document.getElementById('imagesPreview');
	const dt = new DataTransfer();

	imgInput.addEventListener('change', () => {
		// 선택된 파일을 DataTransfer에 추가
		Array.from(imgInput.files).forEach(file => dt.items.add(file));

		// 최대 4장 제한
		if (dt.items.length > 4) {
			alert('최대 4장까지 업로드 가능합니다.');
			// 넘친 만큼 뒤에서부터 삭제
			while (dt.items.length > 4) {
				dt.items.remove(dt.items.length - 1);
			}
		}

		// input의 FileList 갱신 및 미리보기 렌더
		imgInput.files = dt.files;
		renderImagePreviews();
	});

	function renderImagePreviews() {
		imgPreview.innerHTML = '';

		Array.from(dt.files).forEach((file, idx) => {
			const wrapper = document.createElement('div');
			wrapper.classList.add('preview-item');
			wrapper.innerHTML = `
		  <img src="${URL.createObjectURL(file)}" alt="${file.name}" />
		  <button type="button" class="btn-remove" data-idx="${idx}">&times;</button>
		`;
			imgPreview.append(wrapper);

			// 개별 삭제 버튼
			wrapper.querySelector('.btn-remove')
				.addEventListener('click', () => {
					dt.items.remove(idx);
					imgInput.files = dt.files;
					renderImagePreviews();
				});
		});
	}
});
