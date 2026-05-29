#!/bin/bash

# Script para executar GC Test com logging
# Atividade 1.3 - Interpretando GC Logs

echo "========================================"
echo "GC Test Application - Atividade 1.3"
echo "========================================"
echo ""

# Compilar a classe
echo "[1/3] Compilando GCTestApplication..."
javac src/main/java/br/edu/iftm/estruturas/benchmark/GCTestApplication.java
if [ $? -ne 0 ]; then
    echo "ERRO: Falha na compilacao"
    exit 1
fi

echo ""
echo "[2/3] Limpando gc.log anterior..."
rm -f gc.log

echo ""
echo "[3/3] Executando GC Test com flags de logging..."
echo "Saída será registrada em gc.log"
echo "Pressione CTRL+C para interromper quando quiser"
echo ""

# Executar com flags de GC logging
# -Xms256m -Xmx256m: Define heap mínimo e máximo
# -Xlog:gc*: Log todos os eventos de GC
# file=gc.log: Arquivo de saída
# time,uptime: Inclui timestamp e uptime no log
java -Xms256m -Xmx256m -Xlog:gc*:file=gc.log:time,uptime \
    -cp src/main/java \
    br.edu.iftm.estruturas.benchmark.GCTestApplication

echo ""
echo "[CONCLUIDO] GC log gerado em: gc.log"
echo ""
echo "Para analisar o log, execute:"
echo "  cat gc.log"
echo ""
