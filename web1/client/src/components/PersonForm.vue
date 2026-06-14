<script setup lang="ts">
import {ref, watch} from 'vue';
import {personApi, type PersonDto, type PersonCreateRequest} from '../lib/api';

const props = withDefaults(
    defineProps<{
      person: PersonDto;
    }>(),
    {
      person: () => ({
        id: "",
        name: "",
        email: "",
        birthDate: "",
        phones: [],
        properties: {},
      }),
    }
);

const formPerson = ref<PersonDto>({
  ...props.person,
});

watch(() => props.person, (p) => {
  if (p) {
    formPerson.value = {
      id: p.id,
      name: p.name,
      email: p.email,
      birthDate: p.birthDate,
      phones: p.phones,
      properties: p.properties,
    };
  }
}, {immediate: true});

const addPhone = () => {
  formPerson.value.phones.push({
    number: '',
    region: '',
    type: '',
  });
};

const removePhone = (index: number) => {
  formPerson.value.phones.splice(index, 1);
};

const submitForm = async () => {
  try {
    if (formPerson.value.id) {
      await personApi.update(formPerson.value as PersonDto);
    } else {
      await personApi.create(formPerson.value as PersonCreateRequest);
    }
    window.location.href = '/';
  } catch (err) {
    alert('Error saving person: ' + err);
  }
};
</script>

<template>
  <form @submit.prevent="submitForm" class="bg-base-100 space-y-4 rounded-lg p-6 shadow">
    <input type="hidden" name="id" :value="formPerson.id" v-if="formPerson.id"/>

    <div class="form-control">
      <label class="label"><span class="label-text">Name</span></label>
      <input
          type="text"
          v-model="formPerson.name"
          required
          class="input input-bordered"
          minlength="2"
          maxlength="32"
      />
    </div>

    <div class="form-control">
      <label class="label"><span class="label-text">Email</span></label>
      <input
          type="email"
          v-model="formPerson.email"
          required
          class="input input-bordered"
      />
    </div>

    <div class="form-control">
      <label class="label"><span class="label-text">Birth Date</span></label>
      <input
          type="date"
          v-model="formPerson.birthDate"
          required
          class="input input-bordered"
      />
    </div>

    <div class="divider">Phones</div>
    <div class="space-y-2">
      <div v-for="(phone, index) in (formPerson.phones as any[])" :key="index" class="flex gap-2">
        <input
            type="text"
            placeholder="Number"
            v-model="phone.number"
            class="input input-bordered flex-1"
            required
            minlength="5"
            maxlength="20"
        />
        <input
            type="text"
            placeholder="Region"
            v-model="phone.region"
            class="input input-bordered w-24"
            required
            minlength="2"
            maxlength="2"
        />
        <input
            type="text"
            placeholder="Type"
            v-model="phone.type"
            class="input input-bordered w-32"
            required
        />
        <button type="button" @click="removePhone(index)" class="btn btn-square btn-error">
          ✕
        </button>
      </div>
    </div>
    <button type="button" @click="addPhone" class="btn btn-xs btn-outline mt-2">
      Add Another Phone
    </button>

    <div class="pt-6">
      <button type="submit" class="btn btn-primary w-full">Submit Person</button>
    </div>
  </form>
</template>
