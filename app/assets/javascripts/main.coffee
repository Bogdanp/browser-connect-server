class Client
    constructor: ->
        @socket = new WebSocket("ws://localhost:9000/socket")
        @socket.onopen    = => @log("connection established")
        @socket.onclose   = => @log("connection closed")
        @socket.onerror   = (event) => @log(event.data)
        @socket.onmessage = @onMessage

    onMessage: (event) =>
        request = JSON.parse(event.data)

        @log("received '" + request.action + "'")

        switch request.action
            when "reloadCSS" then @reloadCSS()
            when "reloadPage" then @reloadPage()
            when "evaluateJS" then eval(request.source)
            else @log("action '" + request.action + "' is invalid")

    reloadCSS: ->
        links = document.getElementsByTagName("link")

        for link in links
            if !link.oref
                link.oref = link.href
            link.href = link.oref + "?c=" + (new Date).getTime()

    reloadPage: ->
        document.location.href = document.location.href

    log: (message) ->
        if console then console.log("browser-connect: " + message + ".")

new Client
