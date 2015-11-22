package br.com.developer.redu.api;

import java.util.List;

/**
 * @author igor
 *
 * @param <T> - Tipo do Chat
 */
public interface ChatMethods<T> {
	
	/**
	 * Retorna o chat em questão. O recurso Chat contém todas as mensagens 
	 * trocadas entre dois usuários utilizando o sistema de chat do Redu.
	 * 
	 * @param chatId - id do chat
	 * @return Um chat
	 */
	public T getChat(String chatId);
	
	/**
	 * Retorna todos os chats do usuário em questão.
	 * 
	 * @param userId
	 * @return Uma lista de chats
	 */
	public List<T> getChatsByUser(String userId);
}
