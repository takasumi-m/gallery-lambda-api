# Create Post API

## Endpoint
`POST /api/post`

## Sample curl Command

{path}は環境に合わせて変更する

```bash
curl -i -X POST "http://localhost:8080/api/post" \
-H "Accept: application/json" \
-H "Content-Type: multipart/form-data" \
-F 'requestJson={"caption":"My first post", "location":"Tokyo", "deletePassword":"secret", "tagList":["tag1", "tag2"]}' \
-F 'fileList=@{path}/gallery-lambda-api/sample/image/sample_image_1.png' \
-F 'fileList=@{path}/gallery-lambda-api/sample/image/sample_image_2.png'
```