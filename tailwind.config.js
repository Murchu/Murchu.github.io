module.exports = {
  content: [
    './*.html'
  ],
  theme: {
    extend: {
      fontFamily: {
       MyFont: ['"Golos Text"', "sans-serif"],
      },
      backgroundImage: {
        'hero-pattern': "url('/src/img/pattern.svg')"
      }
    },
  }, 
  plugins: [
    require('@tailwindcss/typography')
  ]
}
