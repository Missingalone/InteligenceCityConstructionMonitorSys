/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        city: { 950: '#061427', 900: '#0B1F3A', 700: '#12345b', 500: '#1677FF', 300: '#63b3ff' },
      },
    },
  },
  plugins: [],
}
