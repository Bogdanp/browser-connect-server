Inspired by [LightTable][1]'s browser connect mode.

A packaged version of the server is available [here][2]. After
unpackaging the zip file, you can run the server by doing `sh start`.
You must have at least Java 7 installed in order to be able to run it.

[1]: http://www.lighttable.com/
[2]: http://192.81.222.81/browser-connect-0.1-SNAPSHOT.zip

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
