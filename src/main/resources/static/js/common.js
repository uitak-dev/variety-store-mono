/**
 * 헤더 메뉴 클릭 활성화.
 */
document.addEventListener('DOMContentLoaded', function () {
    // 현재 요청 URL의 경로 (예: "/admin/users")
    const currentPath = window.location.pathname;

    // 헤더 메뉴 내의 모든 nav-link 선택 (id가 headerMenu인 영역 내)
    const navLinks = document.querySelectorAll('#headerMenu a.nav-link');

    navLinks.forEach(link => {
        // 초기 active 클래스 제거
        link.classList.remove('active');
        const linkHref = link.getAttribute('href');
        // 현재 경로가 linkHref로 시작하면 active 처리
        if (currentPath.indexOf(linkHref) === 0) {
            link.classList.add('active');
        }
    });
});
