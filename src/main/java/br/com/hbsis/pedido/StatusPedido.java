package br.com.hbsis.pedido;

    public enum StatusPedido {

        ATIVO("Ativo"),
        CANCELADO("Cancelado"),
        RETIRADO("Retirado");

        private String descricao;

        StatusPedido(String descricao) {
            this.descricao = descricao;
        }
        public String getDescricao() {
            return descricao;
        }
    }
