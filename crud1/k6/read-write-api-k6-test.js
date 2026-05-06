import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  scenarios: {
    // Scenario 1: Constant load for Reading
    read_scenario: {
      executor: 'constant-vus',
      exec: 'readTest',
      vus: 10,
      duration: '30s',
    },
    // Scenario 2: Ramping load for Writing
    write_scenario: {
      executor: 'ramping-vus',
      exec: 'writeTest',
      startVUs: 0,
      stages: [
        { duration: '10s', target: 5 },
        { duration: '20s', target: 5 },
      ],
      gracefulStop: '5s',
    },
  },
  thresholds: {
    'http_req_duration{scenario:read_scenario}': ['p(95)<200'],
    'http_req_duration{scenario:write_scenario}': ['p(95)<500'],
  },
};

const BASE_URL = 'http://localhost:8080';

export function readTest() {
  const res = http.get(`${BASE_URL}/api/v1/persons`);
  check(res, { 'read status 200': (r) => r.status === 200 });
  sleep(1);
}

export function writeTest() {
  const payload = JSON.stringify({
    name: 'John Doe',
    birthDate: '1990-01-01',
    email: `john.doe.${Math.floor(Math.random() * 1000000)}@example.com`,
    phone: '1234567890',
    properties: {
      key: 'value'
    }
  });

  const params = { headers: { 'Content-Type': 'application/json' } };

  const res = http.post(`${BASE_URL}/api/v1/persons`, payload, params);
  check(res, { 'write status 201': (r) => r.status === 201 });
  sleep(2);
}
