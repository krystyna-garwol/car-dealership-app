import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: process.env.REACT_APP_SERVER_URL
  });

async function sendRequest(type, url, body, cb) {
    switch (type) {
        case "GET":
            axiosInstance.get(url).then(res => {
                cb(res);
            })
            break;
        case "POST":
            axiosInstance.post(url, body).then(res => {
                cb(res);
            })
            break;
        default:
            break;
    }
}

export { sendRequest, axiosInstance }