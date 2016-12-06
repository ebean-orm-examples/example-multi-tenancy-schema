package org.example.domain;

import com.avaje.ebean.Ebean;
import org.example.bootstrap.UserContext;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class ContentTest extends BaseTestCase {

	@Test
	public void insert() {

		UserContext.set("rob", "ten_1");

		Author author = new Author();
		author.setName("rob");
		Content bean = new Content();
		bean.setTitle("Testing 123");
		bean.setByline("Thinking about testing with databases");
		bean.setBody("Lots of interesting stuff to say here");
		author.setContents(Arrays.asList(bean));
		author.save();

		UserContext.set("roger", "ten_1");

		author = new Author();
		author.setName("roger");
		bean = new Content();
		bean.setTitle("Testing integration");
		bean.setByline("More testing databases");
		bean.setBody("Meh");
		author.setContents(Arrays.asList(bean));
		author.save();

		UserContext.set("fi", "ten_2");

		Author OtherAuthor = new Author();
		OtherAuthor.setName("fi");
		Content beanOther = new Content();
		beanOther.setTitle("Banana");
		beanOther.setByline("Yummy and yellow");
		beanOther.setBody("Food content");
		OtherAuthor.setContents(Arrays.asList(beanOther));
		OtherAuthor.save();

		System.out.println("Tenant r1");
		findAll1("ten_1");

		System.out.println("Tenant f1");
		findAll1("ten_2");
		
		System.out.println("--------------------------next round----------------------------\n");
		
		System.out.println("Tenant r1");
		findAll2("ten_1");

		System.out.println("Tenant f1");
		findAll2("ten_2");

	}

	//runs well
	private void findAll1(String tenant) {

		CompletionStage<List<Content>> allForTenant = CompletableFuture.supplyAsync(() -> {
			UserContext.set("other", tenant);
			System.out.println("find all (for the current tenant) " + UserContext.get().getTenantId());
			return Ebean.find(Content.class).findList();
		}).thenApplyAsync(allContents -> {
			allContents.forEach(content -> {
				System.out.println("Author: " + content.getAuthor().getName());
				System.out.println("Content: " + content);
			});
			return allContents;
		});
		try {
			allForTenant.toCompletableFuture().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	//Strange behaviour and eventually throws.
	private void findAll2(String tenant) {

		CompletionStage<List<Content>> allForTenant = CompletableFuture.supplyAsync(() -> {
			UserContext.set("other", tenant);
			System.out.println("find all (for the current tenant) " + UserContext.get().getTenantId());
			return Ebean.find(Content.class).findList();
		});

		try {
			allForTenant.toCompletableFuture().get().forEach(content -> {
				System.out.println("Author: " + content.getAuthor().getName());
				System.out.println("Content: " + content);
			});
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}