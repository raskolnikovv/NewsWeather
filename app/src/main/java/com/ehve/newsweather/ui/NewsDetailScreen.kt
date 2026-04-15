package com.ehve.newsweather.ui // Verifique se o pacote está correto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun NewsDetailScreen(
    title: String?,
    description: String?,
    urlToImage: String?,
    url: String?,
    isFavorite: Boolean, // Estado para saber se é favorito ou não
    onBack: () -> Unit,
    onUrlClick: (String) -> Unit,
    onFavoriteClick: () -> Unit // Função para clicar no coração
) {
    val scrollState = rememberScrollState()

    // O Scaffold básico para estrutura, mas sem TopBar, pois vamos desenhar a nossa
    Scaffold { paddingValues ->
        // Box principal para sobrepor os botões de ação sobre a imagem
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
        ) {

            // 1. CONTEÚDO SCROLLÁVEL (Imagem + Texto)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                // Imagem de destaque no topo (Parecido com a referência)
                AsyncImage(
                    model = urlToImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp) // Altura fixa para a imagem
                        .clip(RoundedCornerShape(24.dp))
                )

                // Área do Texto (Título + Descrição)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                ) {
                    // Título (Maior e em negrito como na referência "Overview")
                    Text(
                        text = title ?: "",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(100.dp))

                    // Separador sutil (Opcional, mas dá um toque)
                    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Descrição Completa
                    Text(
                        text = description ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )

                    // Espaço extra no final para o conteúdo não ficar atrás do botão
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

            // 2. BOTÕES DE AÇÃO SUPERIORES (Voltar e Favorito sobre a imagem)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // Margem interna
                horizontalArrangement = Arrangement.SpaceBetween, // Separa nas pontas
                verticalAlignment = Alignment.Top
            ) {
                // Ícone de Voltar (Circular e com fundo translúcido como na referência)
                FilledIconButton(
                    onClick = onBack,
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.5f), // Fundo preto fumê
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
                }

                // Ícone de Favorito (No canto direito, translúcido)
                FilledIconButton(
                    onClick = onFavoriteClick,
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.5f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(48.dp)
                ) {
                    // Troca o ícone se for favorito ou não
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Favoritar"
                    )
                }
            }

            // 3. BOTÃO DE BAIXO (Ler no Navegador fixo no rodapé)
            Button(
                onClick = { url?.let(onUrlClick) },
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Fixa no fundo
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp), // Botão maior
                shape = RoundedCornerShape(16.dp), // Borda arredondada
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // Fundo preto como na referência
                    contentColor = Color.White
                )
            ) {
                Icon(imageVector = Icons.Default.Language, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Ler Notícia no Navegador", fontWeight = FontWeight.Bold)
            }
        }
    }
}