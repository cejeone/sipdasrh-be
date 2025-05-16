cara jalanin di server pakai target-machine/docker-compose.yml

lalu jalanin command :
docker pull ihsannt/rh-service:latest
docker-compose up -d

akses swagger ada di
http://localhost:8081/swagger-ui/index.html

## API Endpoints

Program Management
- GET `/api/program` 
- GET `/api/program/{id}` 
- POST `/api/program` 
- PUT `/api/program/{id}` 
- DELETE `/api/program/{id}` 

Skema Tanam
- GET `/api/skema-tanam` 
- GET `/api/skema-tanam/{id}` 
- POST `/api/skema-tanam` 
- PUT `/api/skema-tanam/{id}` 
- DELETE `/api/skema-tanam/{id}` 

Jenis Bibit
- GET `/api/jenis-bibit` 
- GET `/api/jenis-bibit/{id}` 
- POST `/api/jenis-bibit` 
- PUT `/api/jenis-bibit/{id}` 
- DELETE `/api/jenis-bibit/{id}` 

Pagu Anggaran
- GET `/api/pagu-anggaran` 
- GET `/api/pagu-anggaran/{id}` 
- POST `/api/pagu-anggaran` 
- PUT `/api/pagu-anggaran/{id}` 
- DELETE `/api/pagu-anggaran/{id}` 