<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { personApi, type PersonDtoPage } from '../lib/api';
import PersonTable from './PersonTable.vue';

const personsPage = ref<PersonDtoPage | null>(null);
const page = ref(0);
const pageSize = 10;
const error = ref("");

const fetchPage = async (newPage: number) => {
  try {
    personsPage.value = await personApi.findPage(newPage, pageSize);
    page.value = newPage;
    
    // Update URL without reloading if window is available
    if (typeof window !== 'undefined') {
      const url = new URL(window.location.href);
      url.searchParams.set('page', newPage.toString());
      window.history.pushState({}, '', url.toString());
    }
  } catch (e) {
    console.error(e);
    error.value = "Failed to load persons.";
  }
};

const deletePerson = async (id: string) => {
  if (confirm("Are you sure?")) {
    try {
      await personApi.delete(id);
      await fetchPage(page.value);
    } catch (err) {
      console.error("Delete failed", err);
      alert("Delete failed");
    }
  }
};

onMounted(() => {
  // Get initial page from URL if present
  if (typeof window !== 'undefined') {
    const url = new URL(window.location.href);
    page.value = parseInt(url.searchParams.get('page') || '0');
  }
  fetchPage(page.value);
});
</script>

<template>
  <div>
    <div v-if="error" class="alert alert-error mb-6 p-8">
      <span>{{ error }}</span>
    </div>
    
    <PersonTable
      v-if="personsPage"
      :persons-page="personsPage"
      :page="page"
      @fetch-page="fetchPage"
      @delete-person="deletePerson"
    />
    <div v-else-if="!error" class="p-8 text-center">
      <span class="loading loading-spinner loading-lg"></span>
      <p class="mt-2">Loading persons...</p>
    </div>
  </div>
</template>
