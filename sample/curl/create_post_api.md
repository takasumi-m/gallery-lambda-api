# Create Post API

## Endpoint
`POST /api/post`

## Sample curl Command

fileListのpathは環境に合わせて変更する

```bash
curl -X POST "http://localhost:8080/api/post" \
-H "Accept: application/json" \
-H "Content-Type: multipart/form-data" \
-F 'requestJson={"caption":"My first post", "location":"Tokyo", "deletePassword":"secret", "tagList":["tag1", "tag2"]}' \
-F 'fileList=@~/Documents/repositories/gallery-lambda-api/sample/image/sample_image_1.png' \
-F 'fileList=@~/Documents/repositories/gallery-lambda-api/sample/image/sample_image_2.png'
```