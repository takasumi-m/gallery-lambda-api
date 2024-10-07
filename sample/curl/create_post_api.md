# Create Post API

## Endpoint
`POST /api/post`

## Sample curl Command

filesのpathは環境に合わせて変更する

```bash
curl -X POST http://localhost:8080/api/post \
-F 'caption=Sample caption' \
-F 'location=Sample location' \
-F 'deletePassword=your_password' \
-F 'files=@"/Users/takasumi/Documents/repositories/gallery-lambda-api/sample/image/sample_image_1.png"' \
-F 'files=@"/Users/takasumi/Documents/repositories/gallery-lambda-api/sample/image/sample_image_2.png"' \
-F 'tags=tag1' \
-F 'tags=tag2'
```