document.addEventListener("DOMContentLoaded", function (params) {
        let jwt
        let paginaListagemSimulacao = 0
        let paginaListagemSimulacaoCliente = 0
        const btnLogin = document.getElementById("btnLogin")
        const btnCriarSimulacao = document.getElementById("btnCriarSimulacao")
        const btnListarSimulacaoPorDia = document.getElementById("btnListarSimulacaoPorDia")
        const btnTelemetria = document.getElementById("btnTelemetria")
        const btnListarSimulacao = document.getElementById("btnListarSimulacao")
        const queryPaginaListaSimulacao = document.getElementById("query-pagina")
        const queryPaginaListaSimulacaoCliente = document.getElementById("query-pagina-simcliente")
        const btnRemovePaginaListagemSimulacao = document.getElementById("btnRemovePaginaListagemSimulacao")
        const btnAddPaginaListagemSimulacao = document.getElementById("btnAddPaginaListagemSimulacao")
        const riscoPathRecomendacao = document.getElementById("riscoPathRecomendacao")
        const btnRecomendacao = document.getElementById("btnRecomendacao")
        const btnPerfil = document.getElementById("btnPerfil")
        const perfilPathUrl = document.getElementById("perfilPathUrl")
        const clientIdPathPerfil = document.getElementById("clientIdPathPerfil")
        const btnLoginErrado = document.getElementById("btnLoginErrado")
        const btnRemovePaginaListagemSimCliente = document.getElementById("btnRemovePaginaListagemSimCliente")
        const btnAddPaginaListagemSimCliente = document.getElementById("btnAddPaginaListagemSimCliente")
        const urlSimCliente = document.getElementById("urlSimCliente")
        const btnListarSimulacaoCliente = document.getElementById("btnListarSimulacaoCliente")

        btnLogin.addEventListener("click", function (e) {
            enviarRequest(e)
        })

        btnLoginErrado.addEventListener("click", function (e) {
            enviarRequest(e)
        })

        btnCriarSimulacao.addEventListener("click", function (e) {
            enviarRequest(e, undefined, posSucessoSimulacao, formatarPayloadCriacao)
        })

        btnListarSimulacao.addEventListener("click", function (e) {
            enviarRequest(e)
        })

        btnListarSimulacaoCliente.addEventListener("click", function (e) {
            enviarRequest(e)
        })

        btnTelemetria.addEventListener("click", function (e) {
            enviarRequest(e)
        })

        btnRecomendacao.addEventListener("click", function (e) {
            enviarRequest(e)
        })

        btnListarSimulacaoPorDia.addEventListener("click", function (e) {
            enviarRequest(e)
        })

        btnPerfil.addEventListener("click", function (e) {
            enviarRequest(e)
        })

        btnRemovePaginaListagemSimulacao.addEventListener("click", function (e) {
            mudaPagina(-1)
        })

        btnRemovePaginaListagemSimCliente.addEventListener("click", function (e) {
            debugger
            mudaPaginaSimCliente(-1)
        })

        btnAddPaginaListagemSimulacao.addEventListener("click", function (e) {
            mudaPagina(1)
        })

        btnAddPaginaListagemSimCliente.addEventListener("click", function (e) {
            mudaPaginaSimCliente(1)
        })

        riscoPathRecomendacao.addEventListener("change", function (e) {
            document.getElementById("riscoPathUrl").textContent = "{URL}/produtos-recomendados/" + e.target.value
        })

        clientIdPathPerfil.addEventListener("keyup", function (e) {
            document.getElementById("perfilPathUrl").textContent = "{URL}/perfil-risco/" + e.target.value
        })

        function enviarRequest(
            e,
            formatResponse = undefined,
            posSucesso = undefined,
            formatPayload = undefined
        ) {
            const target = e.target
            
            const parent = target.parentElement
            const url = parent.parentElement
                .getElementsByClassName("url")[0]
                .textContent
            const payload = parent.parentElement
                .parentElement
                .getElementsByClassName("payload")
            const method = parent.parentElement
                .getElementsByClassName("method")[0]
                .textContent
            const response = parent.parentElement
                .parentElement
                .getElementsByClassName("response-text")[0]
            
            const headers = {}
            if (jwt != undefined) {
                headers["Authorization"] = "Bearer " + jwt
            }

            let data = payload.length != 0 ? JSON.parse(payload[0].textContent) : undefined
            if (formatPayload != undefined) {
                data = formatPayload(payload)
            } 
            
            axios.request({
                method: method,
                url: url,
                data: data,
                headers: headers
            }).then((resp) => {
                if (url.includes("v1/auth")) {
                    jwt = resp.data.jwt
                }

                response.textContent = JSON.stringify(resp.data, undefined, 2)
                parent.parentElement
                    .parentElement
                    .getElementsByClassName("sucesso")[0]
                    .classList.toggle("hiden")

                if (posSucesso != undefined) {
                    posSucesso(resp.data)
                }

            }).catch((err) => {
                const resp = err.response
                if (resp) {
                    response.textContent = JSON.stringify(resp.data, undefined, 2)
                }
            })
        }

        function posSucessoSimulacao(data) {
            const ctx = document.getElementById('progressaoSimulacao');
            
            // Destroy existing chart instance if it exists
            if (ctx.chart) {
            ctx.chart.destroy();
            }

            // Create a new chart instance and store it in the canvas element
            ctx.chart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: data.resultadoSimulacao.progressao.map((_, i) => i + 1 + " mês"),
                datasets: [{
                label: '# Meses',
                data: data.resultadoSimulacao.progressao,
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                y: {
                    beginAtZero: true
                }
                }
            }
            });
        }

        function mudaPagina(valor) {
            let novoValor = paginaListagemSimulacao + valor
            queryPaginaListaSimulacao.textContent = novoValor
            paginaListagemSimulacao = novoValor

            urlListaSimulacao.textContent = "{URL}/simulacoes?pagina=" + paginaListagemSimulacao + "&quantidade=10"
        }

        function mudaPaginaSimCliente(valor) {
            let novoValor = paginaListagemSimulacaoCliente + valor
            queryPaginaListaSimulacaoCliente.textContent = novoValor
            paginaListagemSimulacaoCliente = novoValor

            urlSimCliente.textContent = "{URL}/investimentos/1?pagina=" + novoValor + "&quantidade=10"
        }

        function formatarPayloadCriacao(payload) {
            return JSON.parse(payload[0].value)
        }
    })