<script setup lang="ts">
import type { PersonDtoPage } from '../lib/api';

defineProps<{
  personsPage: PersonDtoPage;
  page: number;
}>();

defineEmits<{
  (e: 'fetch-page', page: number): void;
  (e: 'delete-person', id: string): void;
}>();
</script>

<template>
  <div class="p-8">

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
          <tr v-for="person in personsPage.content" :key="person.id">
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
                @click="$emit('delete-person', person.id)"
                class="btn btn-sm btn-error"
              >
                Delete
              </button>
            </td>
          </tr>
          <tr v-if="personsPage.content.length === 0">
            <td colspan="5" class="text-base-content/50 py-8 text-center">
              No persons found.
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="join mt-6 flex justify-center">
      <button
        @click="$emit('fetch-page', page - 1)"
        :disabled="!personsPage.hasPreviousPage"
        class="join-item btn"
      >
        Previous
      </button>
      <button class="join-item btn">
        Page {{ page + 1 }} of {{ personsPage.pageCount || 1 }}
      </button>
      <button
        @click="$emit('fetch-page', page + 1)"
        :disabled="!personsPage.hasNextPage"
        class="join-item btn"
      >
        Next
      </button>
    </div>
  </div>
</template>
