class SiteHeader extends HTMLElement {
  constructor() {
    super();
  }

  connectedCallback() {
    const gradientClass = this.getAttribute('gradient');
    // If gradientClass is empty string or 'none', don't add a background div
    if (!gradientClass || gradientClass === 'none') {
      this.innerHTML = `
      <link rel="preconnect" href="https://fonts.googleapis.com">
      <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
      <link href="https://fonts.googleapis.com/css2?family=Golos+Text&display=swap" rel="stylesheet">
      <nav class="flex justify-between px-6 py-8 mx-auto sm:max-w-screen-xl sm:mx-auto bg-transparent">
        <a href="https://www.iamcolm.com/">
          <div class="sm:hidden">
            <img class="h-6" src="src/img/logos/SMlogo.svg" alt="logo" style="filter:none;opacity:1;" />
          </div>
          <div class="hidden sm:block">
            <img class="h-6" src="src/img/logos/logo.svg" alt="logo" style="filter:none;opacity:1;" />
          </div>
        </a>
        <div class="hidden font-normal space-x-8 lg:flex text-slate-600">
          <a class="hover:text-teal-700" href="index.html">Home</a>
          <a class="hover:text-teal-700" href="src/Colm_Murphy_CV.pdf">About</a>
          <a class="hover:text-teal-700" href="process.html">Process</a>
        </div>
        <div class="flex lg:hidden z-20">
          <div class=" bg-white flex flex-col justify-center ">
            <div class="flex items-center justify-center ">
              <div class=" relative inline-block text-left dropdown">
                <span class="rounded-md shadow-sm"><button
                    class="inline-flex justify-center w-full px-4 py-2 text-sm font-medium leading-5 text-gray-700 transition duration-150 ease-in-out bg-white border border-gray-300 rounded-md hover:text-gray-500 focus:outline-none focus:border-blue-300 focus:shadow-outline-blue active:bg-gray-50 active:text-gray-800"
                    type="button" aria-haspopup="true" aria-expanded="true"
                    aria-controls="headlessui-menu-items-117">
                    <span>More</span>
                    <svg class="w-5 h-5 ml-2 -mr-1" viewBox="0 0 20 20" fill="currentColor">
                      <path fill-rule="evenodd"
                        d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                        clip-rule="evenodd"></path>
                    </svg>
                  </button></span>
                <div
                  class="opacity-0 invisible dropdown-menu transition-all duration-300 transform origin-top-right -translate-y-2 scale-95 z-10">
                  <div class="absolute right-0 w-56 mt-2 origin-top-right bg-white border border-gray-200 divide-y divide-gray-100 rounded-md shadow-lg outline-none"
                    aria-labelledby="headlessui-menu-button-1" id="headlessui-menu-items-117" role="menu">
                    <a href="index.html">
                      <div class="px-4 py-3">
                        <p class="text-sm leading-5">Home</p>
                      </div>
                    </a>
                    <div class="px-4 py-3">
                      <a href="src/Colm_Murphy_CV.pdf">
                        <p class="text-sm leading-5">About</p>
                    </div>
                    </a>
                    <a href="process.html">
                      <div class="px-4 py-3">
                        <p class="text-sm leading-5">Process</p>
                      </div>
                    </a>
                  </div>

                </div>
              </div>
            </div>
      </nav>
      `;
    } else {
      this.innerHTML = `
      <link rel="preconnect" href="https://fonts.googleapis.com">
      <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
      <link href="https://fonts.googleapis.com/css2?family=Golos+Text&display=swap" rel="stylesheet">
      <div class="${gradientClass} z-10">
        <nav class="flex justify-between px-6 py-8 mx-auto sm:max-w-screen-xl sm:mx-auto ">
          <a href="https://www.iamcolm.com/">
            <div class="sm:hidden">
              <img class="h-6" src="src/img/logos/SMlogoAlt.svg" alt="logo" />
            </div>
            <div class="hidden sm:block">
              <img class="h-6" src="src/img/logos/logoAlt.svg" alt="logo" />
            </div>
          </a>
          <div class="hidden text-slate-200 font-normal space-x-8 lg:flex">
            <a class="hover:text-white" href="index.html">Home</a>
            <a class="hover:text-white" href="src/Colm_Murphy_CV.pdf">About</a>
            <a class="hover:text-white" href="process.html">Process</a>
          </div>
          <div class="flex lg:hidden z-20">
            <div class=" bg-white flex flex-col justify-center ">
              <div class="flex items-center justify-center ">
                <div class=" relative inline-block text-left dropdown">
                  <span class="rounded-md shadow-sm"><button
                      class="inline-flex justify-center w-full px-4 py-2 text-sm font-medium leading-5 text-gray-700 transition duration-150 ease-in-out bg-white border border-gray-300 rounded-md hover:text-gray-500 focus:outline-none focus:border-blue-300 focus:shadow-outline-blue active:bg-gray-50 active:text-gray-800"
                      type="button" aria-haspopup="true" aria-expanded="true"
                      aria-controls="headlessui-menu-items-117">
                      <span>More</span>
                      <svg class="w-5 h-5 ml-2 -mr-1" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd"
                          d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                          clip-rule="evenodd"></path>
                      </svg>
                    </button></span>
                  <div
                    class="opacity-0 invisible dropdown-menu transition-all duration-300 transform origin-top-right -translate-y-2 scale-95 z-10">
                    <div class="absolute right-0 w-56 mt-2 origin-top-right bg-white border border-gray-200 divide-y divide-gray-100 rounded-md shadow-lg outline-none"
                      aria-labelledby="headlessui-menu-button-1" id="headlessui-menu-items-117" role="menu">
                      <a href="index.html">
                        <div class="px-4 py-3">
                          <p class="text-sm leading-5">Home</p>
                        </div>
                      </a>
                      <div class="px-4 py-3">
                        <a href="src/Colm_Murphy_CV.pdf">
                          <p class="text-sm leading-5">About</p>
                      </div>
                      </a>
                      <a href="process.html">
                        <div class="px-4 py-3">
                          <p class="text-sm leading-5">Process</p>
                        </div>
                      </a>
                    </div>

                  </div>
                </div>
              </div>
        </nav>
      </div>
      `;
    }
  }
}

customElements.define('site-header', SiteHeader);