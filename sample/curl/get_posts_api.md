# Create Post API

## Endpoint
`GET /api/posts`

## Sample curl Command

```bash
curl -i -X GET "http://localhost:8080/api/posts?caption=second&location=Kyoto&postDatetime=2024-10-11T14:59:23&tagList=tag5&tagList=tag6" \
-H "Content-Type: application/json"
```