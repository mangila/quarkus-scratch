// @ts-check
import { defineConfig } from 'astro/config';

import vue from '@astrojs/vue';
import partytown from '@astrojs/partytown';
import tailwindcss from "@tailwindcss/vite";

import icon from 'astro-icon';

// https://astro.build/config
export default defineConfig({
  integrations: [vue(), partytown(), icon()],
  vite: {
    plugins: [tailwindcss()],
  },
});