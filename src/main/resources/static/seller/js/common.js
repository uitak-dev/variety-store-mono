/**
 * 좌측 사이드바 메뉴 클릭 활성화.
 */
document.addEventListener('DOMContentLoaded', function () {
    // 현재 브라우저의 URL 전체 정보 객체
    const currentUrl = new URL(window.location.href);
    // 좌측 사이드바 내의 모든 nav-link 선택 (id가 asideMenu인 영역 내)
    const navLinks = document.querySelectorAll('#asideMenu a.nav-link');

    navLinks.forEach(link => {
        // 초기 active 클래스 제거
        link.classList.remove('active');
        // 링크의 href에서 URL 객체 생성 (상대 경로도 보정)
        const linkHref = link.getAttribute('href');
        const linkUrl = new URL(linkHref, window.location.origin);

        if (linkUrl.search) {
            // 쿼리 스트링이 있는 경우, pathname 일부와 "userType" 파라미터 비교.
            const currentUserType = currentUrl.searchParams.get('userType');
            const linkUserType = linkUrl.searchParams.get('userType');
            if (currentUrl.pathname.indexOf(linkUrl.pathname) === 0 && currentUserType === linkUserType) {
                link.classList.add('active');
            }
        } else {
            // 쿼리 스트링이 없는 경우: 현재 URL의 pathname이 해당 링크의 pathname으로 시작하면 active 처리
            if (currentUrl.pathname.indexOf(linkUrl.pathname) === 0) {
                link.classList.add('active');
            }
        }

        // 만약 active 처리된 링크가 아코디언 내부에 있다면 부모 아코디언을 펼치도록 처리
        if (link.classList.contains('active')) {
            // 가장 가까운 아코디언 컨테이너(accordion-collapse)를 찾음
            const accordionCollapse = link.closest('.accordion-collapse');
            if (accordionCollapse) {
                // 아코디언 컨테이너에 'show' 클래스를 추가하여 펼침
                accordionCollapse.classList.add('show');
                // 해당 아코디언 버튼(토글)을 찾아서 collapsed 클래스를 제거 및 aria-expanded 속성 업데이트
                const accordionButton = accordionCollapse.previousElementSibling.querySelector('.accordion-button');
                if (accordionButton) {
                    accordionButton.classList.remove('collapsed');
                    accordionButton.setAttribute('aria-expanded', 'true');
                }
            }
        }
    });
});
