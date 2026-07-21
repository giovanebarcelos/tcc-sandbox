Olá {{ nome }},
Seu pedido #{{ numeroPedido }} foi recebido.

{% if pago %}
Pagamento confirmado! Estamos preparando o envio.
{% else %}
Ainda não registramos o pagamento. Utilize o link enviado por e-mail.
{% endif %}

Obrigado por comprar conosco!
