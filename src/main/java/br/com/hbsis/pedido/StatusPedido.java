package br.com.hbsis.pedido;

    public enum StatusPedido {

        ATIVO("Ativo"),
        CANCELADO("Cancelado"),
        RETIRADO("Retirado");

        private String status;

        StatusPedido(String status) {
            this.status = status;
        }
        public String getStatus() {
            return status;
        }
    }


