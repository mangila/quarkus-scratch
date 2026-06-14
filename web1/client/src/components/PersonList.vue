<script setup lang="ts">
import { ref } from 'vue';
import { personApi, type PersonDtoPage } from '../lib/api';

const props = defineProps<{
  initialPersonsPage: PersonDtoPage;
  initialPage: number;
  pageSize: number;
}>();

const personsPage = ref<PersonDtoPage>(props.initialPersonsPage);
const page = ref(props.initialPage);
const error = ref("");

const fetchPage = async (newPage: number) => {
  try {
    personsPage.value = await personApi.findPage(newPage, props.pageSize);
    page.value = newPage;
    const url = new URL(window.location.href);
    url.searchParams.set('page', newPage.toString());
    window.history.pushState({}, '', url.toString());
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
</script>

<template>
  <div class="p-8">
    <div class="mb-6 flex items-center justify-between">
      <h1 class="text-primary text-3xl font-bold">Persons Management</h1>
      <a href="/create" class="btn btn-primary">Add New Person</a>
    </div>

    <div v-if="error" class="alert alert-error mb-6">
      <span>{{ error }}</span>
    </div>

    <div class="bg-base-100 overflow-x-auto rounded-lg shadow">
      <table class="table w-full">
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Birth Date</th>
            <th>Phones</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="person in personsPage?.content" :key="person.id">
            <td>{{ person.name }}</td>
            <td>{{ person.email }}</td>
            <td>{{ person.birthDate }}</td>
            <td>
              <div v-for="(p, i) in person.phones" :key="i" class="badge badge-ghost badge-sm mr-1">
                {{ p.number }} ({{ p.type }})
              </div>
            </td>
            <td class="flex gap-2">
              <a :href="`/edit?id=${person.id}`" class="btn btn-sm btn-info">
                Edit
              </a>
              <button
                @click="deletePerson(person.id)"
                class="btn btn-sm btn-error"
              >
                Delete
              </button>
            </td>
          </tr>
          <tr v-if="personsPage?.content.length === 0">
            <td colspan="5" class="text-base-content/50 py-8 text-center">
              No persons found.
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="join mt-6 flex justify-center">
      <button
        @click="fetchPage(page - 1)"
        :disabled="!personsPage?.hasPreviousPage"
        class="join-item btn"
      >
        Previous
      </button>
      <button class="join-item btn">
        Page {{ page + 1 }} of {{ personsPage?.pageCount || 1 }}
      </button>
      <button
        @click="fetchPage(page + 1)"
        :disabled="!personsPage?.hasNextPage"
        class="join-item btn"
      >
        Next
      </button>
    </div>
  </div>
</template>
