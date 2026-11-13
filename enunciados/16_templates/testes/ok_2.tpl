{% set pedido = "1234" %}{% set itens = [] %}{% set cliente = "penélope" %}Pedido #{{ pedido }} de {{ cliente | capitalize }}
{% if itens %}Itens: {{ itens }}{% else %}{% if not itens %}(pedido vazio){% endif %}{% endif %}
Total de caracteres do nome: {{ cliente | length }}
