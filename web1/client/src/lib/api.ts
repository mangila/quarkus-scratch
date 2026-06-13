export interface PhoneDto {
  number: string;
  region: string;
  type: string;
}

export interface PersonDto {
  id: string;
  name: string;
  birthDate: string; // ISO format
  email: string;
  phones: PhoneDto[];
  properties: Record<string, string>;
}

export interface PersonCreateRequest {
  name: string;
  birthDate: string; // ISO format
  email: string;
  phones: PhoneDto[];
  properties: Record<string, string>;
}

export interface PersonDtoPage {
  content: PersonDto[];
  totalCount: number;
  pageCount: number;
  hasNextPage: boolean;
  hasPreviousPage: boolean;
}

const API_BASE_URL = 'http://localhost:8080/api/v1/persons';

export const personApi = {
  async findPage(page = 0, size = 20): Promise<PersonDtoPage> {
    const response = await fetch(`${API_BASE_URL}?page=${page}&size=${size}`);
    if (!response.ok) throw new Error('Failed to fetch persons');
    return response.json();
  },

  async findById(id: string): Promise<PersonDto> {
    const response = await fetch(`${API_BASE_URL}/${id}`);
    if (!response.ok) throw new Error('Failed to fetch person');
    return response.json();
  },

  async create(person: PersonCreateRequest): Promise<void> {
    const response = await fetch(API_BASE_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(person),
    });
    if (!response.ok) throw new Error('Failed to create person');
  },

  async update(person: PersonDto): Promise<void> {
    const response = await fetch(API_BASE_URL, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(person),
    });
    if (!response.ok) throw new Error('Failed to update person');
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete person');
  },
};
