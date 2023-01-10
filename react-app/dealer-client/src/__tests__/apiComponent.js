import { sendRequest, axiosInstance } from '../components/api';
import MockAdapter from 'axios-mock-adapter';

const mockedAxios = new MockAdapter(axiosInstance);

test('Returns correct data on GET', (done) => {
    const data = { "content": "abc" };

    mockedAxios.onGet("/info").reply(200, data);

    sendRequest("GET", "/info", {}, (res) => {
        expect(JSON.stringify(res.data)).toBe(JSON.stringify(data));
        done();
    });
    
});

test('Returns correct data on POST', (done) => {
    const data = { "content": "abc" };

    mockedAxios.onPost("/info", {"test": true}).reply(200, data);

    sendRequest("POST", "/info", {"test": true}, (res) => {
        expect(JSON.stringify(res.data)).toBe(JSON.stringify(data));
        done();
    });
    
});