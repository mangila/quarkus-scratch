import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
    scenarios: {
        bulk_scenario: {
            executor: 'constant-vus',
            vus: 5,
            duration: '30s',
        },
    },
    thresholds: {
        'http_req_duration': ['p(95)<1000'],
        'http_req_failed': ['rate<0.01'],
    },
};

const BASE_URL = 'http://localhost:8080';

export default function () {
    const bulkSize = 101;
    const persons = [];

    for (let i = 0; i < bulkSize; i++) {
        persons.push({
            name: `Bulk Person ${i}`,
            birthDate: '2000-01-01',
            email: `bulk.${i}.${Math.floor(Math.random() * 1000000)}@example.com`,
            phones: [
                {
                    "number": `0736791310`,
                    "region": "SE",
                    "type": "home"
                }
            ],
            properties: {
                "batch": "load-test"
            }
        });
    }

    const payload = JSON.stringify(persons);
    const params = {headers: {'Content-Type': 'application/json'}};

    const res = http.post(`${BASE_URL}/api/v1/persons/bulk`, payload, params);

    check(res, {
        'bulk create status 200': (r) => r.status === 200,
        'has correct count': (r) => r.json().count === bulkSize,
    });

    sleep(1);
}
