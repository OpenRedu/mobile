package br.com.developer.redu.api;

import java.util.List;

/**
 * @author igor
 *
 * @param <T> - Tipo da Mensagem
 */
public interface ChatMessageMethods<T> {
	/**
	 * Retorna uma mensagem do chat.
	 * 
	 * @param chatMessageId - Id da mensagem
	 * @return Uma mensagem
	 */
	public T getChatMessage(String chatMessageId);
	
	
	/**
	 * Retorna todas as mensagens contidas num chat.
	 * 
	 * @param chatId - Id do Chat
	 * @return Uma lista de chatMessages
	 */
	public List<T> getChatMessagesByChat(String chatId);
}
