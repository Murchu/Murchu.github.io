module.exports = {
  content: [
    './*.html'
  ],
  theme: {
    extend: {
      fontFamily: {
       MyFont: ["Open Sans", "sans-serif"],
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
