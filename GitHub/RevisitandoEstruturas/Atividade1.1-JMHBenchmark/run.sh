#!/bin/bash

# Script para facilitar a execução do benchmark JMH
# Atividade 1.1 - Benchmark Básico: HashMap vs TreeMap

echo "========================================"
echo "JMH Benchmark - Estruturas de Dados"
echo "========================================"
echo ""

# Verificar se Maven está disponível
if ! command -v mvn &> /dev/null; then
    echo "ERRO: Maven não está instalado ou não está no PATH"
    echo "Instale Maven em: https://maven.apache.org/download.cgi"
    exit 1
fi

# Compilar e gerar o JAR
echo "[1/2] Compilando projeto e gerando JAR executável..."
echo ""
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "ERRO: Falha na compilação"
    exit 1
fi

echo ""
echo "[2/2] Executando benchmarks..."
echo ""
java -jar target/benchmarks.jar
