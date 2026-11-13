{% set nome = "Muttley" %}{% set vip = "sim" %}{% set cursos = ["Compiladores", "Teoria da Computação", "IA"] %}Olá, {{ nome | upper }}!
{% if vip %}Você é cliente VIP.{% else %}Você é um cliente comum.{% endif %}
Cursos matriculados ({{ cursos | length }}):
{% for c in cursos %}  - {{ c }}
{% endfor %}{% if not desconto %}Nenhum desconto aplicado.{% endif %}
