# Create Post API

## Endpoint
`GET /api/posts`

## Sample curl Command

```bash
curl -i -X GET "http://localhost:8080/api/posts?caption=sunset&location=Tokyo&postDate=2024-10-10&postDatetime=2024-10-10T10%3A30%3A00&tagList=travel%2Cphotography" \
-H "Content-Type: application/json"
```