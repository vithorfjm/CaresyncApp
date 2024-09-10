const modal = document.querySelector(".modal-excluir");
const botoesDelete = document.querySelectorAll(".apagar");
const formDelete = document.querySelector(".formulario-delecao");
const botaoCancelar = document.querySelector("#cancelar");

botoesDelete.forEach(btn => {
    btn.addEventListener("click", () => {
        modal.style.display = "flex";
        const id = btn.dataset.id;
        formDelete.action = `excluir/${id}`;
    });
});

botaoCancelar.addEventListener("click", () => {
    modal.style.display = "none";
})