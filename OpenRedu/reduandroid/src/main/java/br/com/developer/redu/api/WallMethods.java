package br.com.developer.redu.api;

import java.util.List;

/**
 * @author igor
 *
 * @param <T> - Tipo do status
 */
public interface WallMethods<T> {
	
    /**
     * Retorna um status com o ID especificado.
     * @param statusId - ID do status
     * @return Um status
     */
    public T getStatus(String statusId);

    /**
     * Retorna as respostas de um status. É aplicado aos tipos Help e Activity.
     * @param statusId - ID do status
     * @return Uma lista de status
     */
    public List<T> getAnswers(String statusId);

    /**
     * Cria um status do tipo Answer em resposta a um Activity ou Help.
     * @param statusId - [OBRIGATÓRIO] ID do status em questão. Somente do tipo Activity ou Help.
     * @param text - [OBRIGATÓRIO] É uma resposta a um comentário ou um pedido de ajuda. Máximo 800 caracteres
     * @return O status criado
     */
    public T postAnswer(String statusId, String text);

    /**
     * Retorna todos os status do usuário. Também é possível filtrar pelo tipo. Caso :type não seja informado 
     * é retornado os status do tipo Activity, Help e Log do usuário.
     * @param userId - [OBRIGATÓRIO] - ID do usuário em questão
     * @param type - [OPCIONAL] - Filtragem pelo tipo de status. Pode ser do tipo Activity, Help ou Log
     * @param page - [OPCIONAL] - Paginação dos resultados. As páginas possuem 25 entradas. Padrão é 1. Numérico
     * @return Uma lista de Statuses
     */
    public List<T> getStatusesByUser(String userId, String type, String page);
    
    /**
     * Retorna todos os status do usuário. Também é possível filtrar pelo tipo. 
     * Caso :type não seja informado é retornado os status do tipo Activity, Help e Log do usuário.
     * Retorna todos os status do usuário e dos cursos que o usuário faz parte. Diferente do getStatusesByUser, 
     * aqui são mostradas todas as movimentações que interessam ao usuário. 
     * É igual ao que se vê na página inicial do Redu.
     * @param userId - ID do usuário em questão
     * @param type - [OPICIONAL] Filtragem pelo tipo de status. Pode ser do tipo Activity, Help ou Log
     * @param page - [OPICIONAL] Paginação dos resultados. As páginas possuem 25 entradas. Padrão é 1. Numérico
     * @return Uma lista de statuses
     */
    public List<T> getStatusesTimelineByUser(String userId, String type, String page);
    
    /**
     * Retorna todos os status do usuário. Também é possível filtrar pelo tipo. 
     * Caso :type não seja informado é retornado os status do tipo Activity, Help e Log do usuário.
     * Retorna todos os status do usuário e dos cursos que o usuário faz parte. Diferente do getStatusesByUser, 
     * aqui são mostradas todas as movimentações que interessam ao usuário. 
     * É igual ao que se vê na página inicial do Redu.
     * @param userId - ID do usuário em questão
     * @param logeableType - [OPICIONAL] Filtragem pelo tipo de status. Pode ser do Enrollment, Friendship, Course, Subject ou Lecture
     * @param page - [OPICIONAL] Paginação dos resultados. As páginas possuem 25 entradas. Padrão é 1. Numérico
     * @return Uma lista de statuses
     */
    public List<T> getStatusesTimelineLogByUser(String userId, String logeableType, String page);
    
    /**
     * Retorna todos os status da disciplina e das suas aulas. Diferente do getStatusesBySpace, 
     * aqui são agregados tanto as postagens da disciplina quanto as das suas aulas. 
     * É igual ao que se vê no mural da disciplina.
     * 
     * @param spaceId - ID do space
	 * @param type - [OPICIONAL] Filtragem pelo tipo de status. Pode ser do tipo Activity, Help ou Log
     * @param page - [OPICIONAL] Paginação dos resultados. As páginas possuem 25 entradas. Padrão é 1. Numérico
     * @return Uma lista de statuses
     */
    public List<T> getStatusesTimelineBySpace(String spaceId, String type, String page);

    
    /**
     * Cria um status do tipo Activity no Mural do usuário.
     * 
     * @param userId - ID do usuário em questão
     * @param status - Conteúdo do status. Máximo 800 caracteres
     * @return O status criado
     */
    public T postStatusUser(String userId, String status);
    
    /**
     * Retorna todos os status do mural de uma disciplina. Há possibilidade de 
     * filtrar os status da disciplina pelo tipo. É importante preceber 
     * que os posts listados aqui se limitam àqueles postado diretamente na disciplina.
     * 
     * @param spaceId - ID do space
	 * @param type - [OPICIONAL] Filtragem pelo tipo de status. Pode ser do tipo Activity, Help ou Log
     * @param page - [OPICIONAL] Paginação dos resultados. As páginas possuem 25 entradas. Padrão é 1. Numérico
     * @return Uma lista de statuses
     */
    public List<T> getStatusesBySpace(String spaceId, String type, String page);

    /**
     * Cria um status no Mural da disciplina.
     * 
     * @param spaceId - ID do space em questão
     * @param status - Conteúdo do status. Máximo 800 caracteres
     * @return O status criado
     */
    public T postStatusSpace(String spaceId, String text);
    
    
    /**
     * Retorna todos os status do mural de uma aula. 
     * @param lectureId - ID da lecture em questão
     * @return Uma lista de statuses
     */
    public List<T> getStatusesByLecture(String lectureId);
    
    /**
     * Cria um status no Mural da aula. Somente neste caso é permitido criar um Help (pedido de ajuda).
     * 
     * @param lectureId - ID da lecture em questão
     * @param status - Conteúdo do status. Máximo 800 caracteres
     * @param type - Tipo do status. Help
     * @return o status criado
     */
    public T postStatusLecture(String lectureId, String status, String type);

    /**
     * Remove o status.
     * @param statusId - ID do status
     */
    public void deleteStatus(String statusId);

}
