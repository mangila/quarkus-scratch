<script setup lang="ts">
import PersonForm from "./PersonForm.vue";
import {onMounted, ref, watch, watchEffect} from 'vue';
import {personApi, type PersonDto} from "../lib/api.ts";

interface Props {
  title: string;
}

const props = defineProps<Props>();
const personId = ref<string>('');
const person = ref<PersonDto>({
  id: '',
  name: '',
  email: '',
  birthDate: '',
  phones: [],
  properties: {},
});

onMounted(() => {
  const urlParams = new URLSearchParams(window.location.search);
  personId.value = urlParams.get('id') || '';
});

watch(personId, async (id) => {
  if (!id) return;
  person.value = await personApi.findById(id);
}, {immediate: true});

</script>

<template>
  <div class="mx-auto max-w-2xl p-8">
    <div class="mb-6">
      <a href="/" class="btn btn-ghost btn-sm mb-4">← Back to List</a>
      <h1 class="text-primary text-3xl font-bold">{{ props.title }}</h1>
    </div>
    <PersonForm :person="person"/>
  </div>
</template>