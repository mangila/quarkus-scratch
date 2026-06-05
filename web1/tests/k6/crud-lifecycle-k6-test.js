import http from 'k6/http';
import {check, sleep, group} from 'k6';

export const options = {
    scenarios: {
        crud_scenario: {
            executor: 'ramping-vus',
            startVUs: 1,
            stages: [
                {duration: '10s', target: 10},
                {duration: '20s', target: 10},
                {duration: '10s', target: 0},
            ],
            gracefulStop: '5s',
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<500'],
        http_req_failed: ['rate<0.01'],
    },
};

const BASE_URL = 'http://localhost:8080';

export default function () {
    const payload = JSON.stringify({
        name: 'Jane Doe',
        birthDate: '1995-05-05',
        email: `jane.doe.${Math.floor(Math.random() * 1000000)}@example.com`,
        phones: [
            {
                "number": "0701234567",
                "region": "SE",
                "type": "mobile"
            }
        ],
        properties: {
            "source": "k6-load-test"
        }
    });

    const params = {headers: {'Content-Type': 'application/json'}};

    let personId;

    group('CRUD Lifecycle', function () {
        // 1. Create
        const createRes = http.post(`${BASE_URL}/api/v1/persons`, payload, params);
        check(createRes, {
            'create status 201': (r) => r.status === 201,
            'has location header': (r) => r.headers['Location'] !== undefined,
        });

        if (createRes.status === 201) {
            const location = createRes.headers['Location'];
            personId = location.split('/').pop();

            // 2. Read (ById)
            const getRes = http.get(`${BASE_URL}/api/v1/persons/${personId}`);
            check(getRes, {
                'get status 200': (r) => r.status === 200,
                'correct id': (r) => r.json().id === personId,
            });

            // 3. Update
            const updatedPayload = JSON.stringify({
                id: personId,
                name: 'Jane Updated',
                birthDate: '1995-05-05',
                email: `jane.updated.${Math.floor(Math.random() * 1000000)}@example.com`,
                phones: [
                    {
                        "number": "0701234567",
                        "region": "SE",
                        "type": "mobile"
                    }
                ],
                properties: {
                    "source": "k6-load-test",
                    "updated": "true"
                }
            });
            const updateRes = http.put(`${BASE_URL}/api/v1/persons`, updatedPayload, params);
            check(updateRes, {
                'update status 204': (r) => r.status === 204,
            });

            // 4. Delete
            const deleteRes = http.del(`${BASE_URL}/api/v1/persons/${personId}`);
            check(deleteRes, {
                'delete status 204': (r) => r.status === 204,
            });
        }
    });

    sleep(1);
}
