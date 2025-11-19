function showError(message) {
    const errorAlert = document.getElementById("errorAlert");
    const errorMessage = document.getElementById("errorMessage");
    errorMessage.textContent = message;
    errorAlert.classList.remove("hidden");
    setTimeout(() => {
        errorAlert.classList.add("hidden");
    }, 3000);
}

function showSuccess(message) {
    const errorAlert = document.getElementById("successAlert");
    const errorMessage = document.getElementById("successMensagem");
    errorMessage.textContent = message;
    errorAlert.classList.remove("hidden");
    setTimeout(() => {
        errorAlert.classList.add("hidden");
    }, 3000);
}

document.addEventListener("DOMContentLoaded", function (params) {
    let jwt
    let jwtDecoded
    let clienteId
    const criarContaForm = document.getElementById("criarContaForm")
    const signupName = document.getElementById("signupName")
    const signupPassword = document.getElementById("signupPassword")
    const signupCpf = document.getElementById("signupCpf")
    const formLogin = document.getElementById("formLogin")
    const formSistema = document.getElementById("formSistema")
    const criarSimulacao = document.getElementById("criarSimulacao")
    const idValorInv = document.getElementById("idValorInv")
    const idTipo = document.getElementById("idTipo")
    const idPrazoMeses = document.getElementById("idPrazoMeses")
    const idClienteId = document.getElementById("idClienteId")
    const retornoSim = document.getElementById("retornoSim")
    const retornoSimNome = document.getElementById("retornoSimNome")
    const retornoSimRisco = document.getElementById("retornoSimRisco")
    const retornoSimValorRet = document.getElementById("retornoSimValorRet")
    const retornoSimValorInv = document.getElementById("retornoSimValorInv")
    const idPerfil = document.getElementById("idPerfil")
    const listaHistorico = document.getElementById("listaHistorico")
    const listaRecomendacoes = document.getElementById("listaRecomendacoes")
    const idRecomendacaoProduto = document.getElementById("idRecomendacaoProduto")

    criarContaForm.addEventListener("click", function (e) {
        const data = {
            nome: signupName.value,
            password: signupPassword.value,
            cpf: signupCpf.value,
        }
        axios.post("/v1/auth/cadastro", data).then((r) => {
            jwt = r.data.jwt
            r.data.jwt = r.data.jwt.substr(0, 10) + "..."
            showSuccess(JSON.stringify(r.data, undefined, 2))
            formLogin.classList.add("hidden")
            formSistema.classList.remove("hidden")
            jwtDecoded = decodeJwt(jwt)
            clienteId = jwtDecoded.payload.clienteId
            idClienteId.value = clienteId
        }).catch((err) => {
            showError(JSON.stringify(err.response.data, undefined, 2))
        })
    })

    criarSimulacao.addEventListener('click', function (e) {
        const data = {
            clienteId: idClienteId.value,
            prazoMeses: idPrazoMeses.value,
            tipoProduto: idTipo.value,
            valor: idValorInv.value,
        }
        const header = {
            Authorization: "Bearer " + jwt
        }
        axios.post("/simular-investimento", data, { headers: header }).then((r) => {
            showSuccess(JSON.stringify(r.data, undefined, 2))
            showGraficoSimulacao(r.data)
            if (retornoSim.classList.contains("hidden")) {
                retornoSim.classList.toggle("hidden")
            }

            retornoSimNome.textContent = r.data.produtoValidado.nome
            retornoSimRisco.textContent = "Risco " + r.data.produtoValidado.risco
            retornoSimValorInv.textContent = "Valor investido: " + data.valor
            retornoSimValorRet.textContent = "Valor retornado: " + r.data.resultadoSimulacao.valorFinal

            getPerfil()
            getHistorico()
        }).catch((err) => {
            showError(JSON.stringify(err.response.data, undefined, 2))
        })
    })

    idRecomendacaoProduto.addEventListener('change', function (e) {
         const header = {
            Authorization: "Bearer " + jwt
        }

        axios.get('/produtos-recomendados/' + e.target.value, { headers: header })
            .then(r => {
                listaRecomendacoes.innerHTML = ""
                r.data.forEach(element => {
                    listaRecomendacoes.innerHTML += `<div class="flex flex-col border-b-2 mb-4">
                        <p class="font-bold text-gray-800 text-2xl">${element.nome}</p>
                        <p class=""><b>Risco:</b>${element.risco}</p>
                        <p class=""><b>Rentabilidade:</b> ${element.rentabilidade * 100}% a.a</p>
                    </div>`
                });
            })
    })

    function showGraficoSimulacao(data) {
        const ctx = document.getElementById('ctxSimulacao');
        
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

    function getPerfil() {
        const header = {
            Authorization: "Bearer " + jwt
        }
        axios.get("/perfil-risco/" + clienteId, { headers: header })
            .then((r) => {
                idPerfil.textContent = r.data.perfil + ", pontuação " + r.data.pontuacao
            })
    }

    function getHistorico() {
        const header = {
            Authorization: "Bearer " + jwt
        }
        axios.get("/investimentos/" + clienteId, { headers: header })
            .then((r) => {
                listaHistorico.innerHTML = ""
                r.data.dados.forEach(element => {
                    listaHistorico.innerHTML += `<div class="flex flex-col border-b-2 mb-4">
                        <p class="font-bold text-gray-800 text-2xl">${element.produto}</p>
                        <p class=""><b>Retorno Final:</b> R$ ${element.valorFinal}</p>
                        <p class=""><b>Prazo de:</b> ${element.prazoMeses} mes(es)</p>
                        <p class=""><b>Data:</b> ${element.dataCriacao}</p>
                    </div>`
                });
            })
    }

    function decodeJwt(token) {
        const parts = token.split('.');
        if (parts.length !== 3) {
            throw new Error("Invalid JWT format: Token must have 3 parts separated by dots.");
        }

        const headerEncoded = parts[0];
        const payloadEncoded = parts[1];

        // Helper function to decode Base64Url
        function base64UrlDecode(str) {
            let base64 = str.replace(/-/g, '+').replace(/_/g, '/');
            switch (base64.length % 4) {
            case 0: break;
            case 2: base64 += '=='; break;
            case 3: base64 += '='; break;
            default: throw new Error('Illegal base64url string!');
            }
            return atob(base64);
        }

        const header = JSON.parse(base64UrlDecode(headerEncoded));
        const payload = JSON.parse(base64UrlDecode(payloadEncoded));

        return { header, payload };
    }
})