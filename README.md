Inspired by LightTable's browser connect mode.

---

#### `GET /ws`

A JavaScript client that connects to the server through WebSockets and
waits for orders. Include this into your templates like so:

```html
<script src="http://localhost:9000/ws"></script>
```

---

#### `GET /reloadCSS`

Order the client (`/ws`) to reload all the CSS files in all the
templates that it has been loaded in.

```bash
curl localhost:9000/reloadCSS
```

---

#### `POST /evaluateJS`

Order the client (`/ws`) to evaluate as JavaScript any arbitrary bit of
text that is passed in as the body of the `POST` request.

```bash
curl -XPOST localhost:9000/evaluateJS -d 'alert("Hello, World!");'
```
