import http from 'k6/http';
import { sleep } from 'k6';
export const options = {
  vus: 500,
  duration: '30s',
};
export default function () {
  http.get('http://localhost:8080/hello/getAuthor/2');
  sleep(1);
}
