/**
 * 상품 수정 Form 관리
 */
class ProductEditForm {
    constructor() {
        this.form = document.getElementById('productForm');
        this.optionsContainer = document.getElementById('productOptionsContainer');
        this.cards = [];
        this.counter = 0;
        this._bind();
        this._hydrateInitial();
    }

    _hydrateInitial() {
        if (!window.initialProductOptions) return;
        window.initialProductOptions.forEach(opt => {
            this._addCard(opt.global, {
                name: opt.name,
                templateId: opt.globalOptionId,
                optionValues: opt.productOptionValues.map(v => ({
                    optionValue: v.productOptionValue,
                    additionalPrice: v.additionalPrice,
                    stockQuantity: v.stockQuantity,
                    global: v.global
                }))
            });
        });
    }

    _bind() {
        // 수동 옵션 추가
        document.getElementById('addManualOptionButton')?.addEventListener('click', () => this._addCard(false));

        // 상품 타입 토글
        document.querySelectorAll("input[name='single']").forEach(input => input.addEventListener('change', () => {
            const isOption = document.getElementById('optionProduct').checked;
            document.getElementById('optionSection').style.display = isOption ? 'block' : 'none';
            document.getElementById('singleSection').style.display = isOption ? 'none' : 'block';
        }));

        // 템플릿 불러오기
        document.getElementById('loadTemplateButton')?.addEventListener('click', async () => {
            const cid = document.getElementById('categoryId').value;
            if (!cid) {
                alert('카테고리를 먼저 선택하세요.');
                return;
            }

            const res = await fetch(`/api/categories/${cid}/options`);
            if (!res.ok) throw new Error('템플릿 로드 실패');
            const templates = await res.json();
            this._showTemplates(templates);
            new bootstrap.Modal(document.getElementById('templateModal')).show();
        });

        // 템플릿 선택
        $('#templateModalContainer').on('click', '.option-template-card', e => {
            const template = JSON.parse($(e.currentTarget).attr('data-template'));
            this._addCard(true, {
                name: template.name,
                templateId: template.id,
                optionValues: template.globalOptionValues
            });
            this.counter++;
            bootstrap.Modal.getInstance(document.getElementById('templateModal')).hide();
        });

        // 폼 제출
        this.form.addEventListener('submit', e => this._submit(e));
    }

    _addCard(isTpl, data = {}) {
        const card = new OptionCard(this.counter++, isTpl, data, removed => {
            this.cards = this.cards.filter(c => c !== removed);
        });
        this.cards.push(card);
        this.optionsContainer.append(card.element);
    }

    _showTemplates(arr) {
        const container = $('#templateModalContainer').empty();
        if (!arr.length) return container.html('<p>템플릿이 없습니다.</p>');
        arr.forEach(t => {
            container.append(`
                <div class="col">
                    <div class="card option-template-card" data-template='${JSON.stringify(t)}'>
                        <div class="card-body">
                            <h5 class="card-title">${t.name}</h5>
                            ${t.globalOptionValues?.length ? `<ul class="list-group list-group-flush">${t.globalOptionValues.map(v => `<li class="list-group-item">${v.optionValue}</li>`).join('')}</ul>` : ''}
                        </div>
                    </div>
                </div>
            `);
        });
    }

    _gather() {
        return {
            id: document.getElementById('id').value,
            name: document.getElementById('name').value,
            description: document.getElementById('description').value,
            basePrice: document.getElementById('basePrice').value,
            manufactureDate: document.getElementById('manufactureDate').value,
            stockQuantity: document.getElementById('stockQuantity')?.value || null,
            single: document.querySelector("input[name='single']:checked").value,
            categoryId: document.getElementById('categoryId').value,
            productOptions: this.cards.map(c => c.getData())
        };
    }

    async _submit(e) {
        e.preventDefault();
        try {
            // 썸네일 및 추가 이미지 업로드
            let thumbData = [initialThumbnail];         // 기본적으로 기존 썸네일 데이터 유지
            let imagesData = initialImages.slice();   // 기존 상품 이미지 데이터 복사

            // 썸네일 업로드 (파일이 존재하면 업로드 수행)
            const thumbInput = document.getElementById('thumbnail');
            if (thumbInput.files.length > 0) {
                thumbData = await uploadFiles(thumbInput.files);
            }

            // 추가 이미지 업로드 (파일이 존재하면 업로드 수행)
            const imagesInput = document.getElementById('images');
            if (imagesInput.files.length > 0) {
                // 총 추가 이미지 수가 4장을 초과하는지 체크
                const totalNewCount = imagesInput.files.length + imagesData.length;
                if (totalNewCount > 4) {
                    alert("상품 이미지는 최대 4장까지만 업로드할 수 있습니다.(이미 등록된 이미지 포함)");
                    return;
                }
                imagesData.push(...await uploadFiles(imagesInput.files));
            }

            // 본문 데이터
            const data = this._gather();
            data.thumbnail = thumbData[0];
            data.images = imagesData;
            const res = await fetch(`/seller/products/${data.id}/edit`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (!res.ok) throw new Error('상품 수정 실패');
            const result = await res.json();

            window.location.replace(`/seller/products/${result.id}`);
        } catch (err) {
            console.error(err);
            alert(err.message);
        }
    }
}

// 파일 업로드 함수
async function uploadFiles(files) {
    const productId = document.getElementById('id').value;

    const fm = new FormData();
    // 모든 파일을 'files' 키에 추가
    for (const file of files) {
        fm.append('files', file);
    }

    const res = await fetch(`/api/products/images`, {
        method: 'POST',
        body: fm
    });

    if (!res.ok) throw new Error('이미지 업로드 실패');
    return res.json(); // 업로드된 파일 데이터 반환(배열)
}


// 지정한 이미지 URL을 서버에 삭제 요청
async function deleteImage(storeFileName) {
    try {
        const productId = document.getElementById('id').value;
        const response = await fetch(`/api/products/${productId}/images/${storeFileName}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(storeFileName)
        });
        if (!response.ok) {
            throw new Error('삭제 실패');
        }
        return true;
    } catch (error) {
        alert("이미지 삭제 중 오류 발생: " + error);
        return false;
    }
}

// 삭제 버튼 이벤트 핸들러 (썸네일 및 추가 이미지 삭제)
document.addEventListener('click', async function (event) {
    if (event.target.matches('.delete-thumbnail-btn')) {
        var imageUrl = event.target.getAttribute('data-image-url');
        if (confirm("썸네일 이미지를 삭제하시겠습니까?")) {
            if (await deleteImage(imageUrl)) {
                initialThumbnail = "";
                document.getElementById('currentThumbnail').innerHTML = "<p>썸네일이 삭제되었습니다.</p>";
            }
        }
    }
    if (event.target.matches('.delete-additional-btn')) {
        var imageUrl = event.target.getAttribute('data-image-url');

        if (confirm("해당 상품 이미지를 삭제하시겠습니까?")) {
            if (await deleteImage(imageUrl)) {
                var liElement = event.target.closest('li');
                if (liElement) {
                    liElement.remove();
                }
                initialImages = initialImages.filter(function (uploadFile) {
                    return uploadFile.storeFileName !== imageUrl;
                });
            }
        }
    }
});

// 페이지 로드시 인스턴스화
document.addEventListener('DOMContentLoaded', () => new ProductEditForm());