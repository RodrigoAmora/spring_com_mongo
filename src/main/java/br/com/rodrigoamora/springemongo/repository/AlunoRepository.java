package br.com.rodrigoamora.springemongo.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import br.com.rodrigoamora.springemongo.codecs.AlunoCodec;
import br.com.rodrigoamora.springemongo.model.Aluno;

@Repository
public class AlunoRepository {
	
	private MongoClient cliente;
	private MongoDatabase bancaDeDados;
	
	private void criarConexao() {
		Codec<Document> codec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
		AlunoCodec alunoCodec = new AlunoCodec(codec);
		CodecRegistry registro = CodecRegistries.fromRegistries(CodecRegistries.fromCodecs(alunoCodec),
																MongoClientSettings.getDefaultCodecRegistry());

		MongoClientSettings opcoes = MongoClientSettings.builder()
														.codecRegistry(registro)
														.build();

		this.cliente = MongoClients.create(opcoes);
		this.bancaDeDados = cliente.getDatabase("test");
	}
	
	public void salvar(Aluno aluno) {
		this.criarConexao();
		
		MongoCollection<Aluno> alunos = this.bancaDeDados.getCollection("alunos", Aluno.class);

		if (aluno.getId() == null) {
			alunos.insertOne(aluno);
		} else {
			alunos.updateOne(Filters.eq("_id", aluno.getId()), new Document("$set", aluno));
		}
		
		this.fecharConexao();
	}

	
	
	public List<Aluno> obterTodosAlunos() {
		this.criarConexao();
		
		MongoCollection<Aluno> alunos = this.bancaDeDados.getCollection("alunos", Aluno.class);
		MongoCursor<Aluno> resultados = alunos.find().iterator();
		
		List<Aluno> alunosEncontrados = this.popularAlunos(resultados);
		
		this.fecharConexao();
		
		return alunosEncontrados;
		
	}
	
	public Aluno obterAlunoPor(String id) {
		this.criarConexao();

		MongoCollection<Aluno> alunos = this.bancaDeDados.getCollection("alunos", Aluno.class);
		Aluno aluno = alunos.find(Filters.eq("_id", new ObjectId(id))).first();

		this.fecharConexao();

		return aluno;
		
	}

	public List<Aluno> pesquisarPor(String nome) {
		this.criarConexao();

		MongoCollection<Aluno> alunoCollection = this.bancaDeDados.getCollection("alunos" , Aluno.class);
		MongoCursor<Aluno> resultados = alunoCollection.find(Filters.eq("nome", nome), Aluno.class).iterator();
		
		List<Aluno> alunos = popularAlunos(resultados);
		
		this.fecharConexao();
		
		return alunos;
	}

	private void fecharConexao() {
		this.cliente.close();
	}
	
	private List<Aluno> popularAlunos(MongoCursor<Aluno> resultados) {
		List<Aluno> alunos = new ArrayList<>();
		while(resultados.hasNext()){
			alunos.add(resultados.next());
		}
		return alunos;
	}

	public List<Aluno> pesquisarPor(String classificacao, double nota) {
		this.criarConexao();
		
		MongoCollection<Aluno> alunoCollection = this.bancaDeDados.getCollection("alunos", Aluno.class);
		MongoCursor<Aluno> resultados = null;
		
		if (classificacao.equals("reprovados")) {
			resultados = alunoCollection.find(Filters.lt("notas", nota)).iterator();
		}else if(classificacao.equals("aprovados")){
			resultados = alunoCollection.find(Filters.gte("notas", nota)).iterator();
		}
		
		List<Aluno> alunos = popularAlunos(resultados);
		
		this.fecharConexao();
		
		return alunos;
	}

	public List<Aluno> pesquisaPorGeolocalizacao(Aluno aluno) {
		this.criarConexao();
		
		MongoCollection<Aluno> alunoCollection = this.bancaDeDados.getCollection("alunos", Aluno.class);
		
		alunoCollection.createIndex(Indexes.geo2dsphere("contato"));
		
		List<Double> coordinates = aluno.getContato().getCoordinates();
		Point pontoReferencia = new Point(new Position(coordinates.get(0), coordinates.get(1)));
		
		MongoCursor<Aluno> resultados = alunoCollection.find(Filters.nearSphere("contato", pontoReferencia, 2000.0, 0.0)).limit(2).skip(1).iterator();
		
		List<Aluno> alunos = popularAlunos(resultados);
		
		this.fecharConexao();

		return alunos;
	}

}
