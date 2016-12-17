package org.example.domain;

import com.avaje.ebean.Ebean;
import org.example.bootstrap.UserContext;
import org.example.domain.Role.RoleName;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
		author.setAddresses(Arrays.asList(new Address("ten_1 Rd")));
		Role role = new Role();
		role.setName(RoleName.ADMIN);

		Author authorForLambda1 = author;
		Role roleForLambda1 = role;
		Ebean.execute(() -> {
			roleForLambda1.save();
			authorForLambda1.setRoles(Arrays.asList(roleForLambda1));
			authorForLambda1.save();
		});

		UserContext.set("roger", "ten_1");

		author = new Author();
		author.setName("roger");
		bean = new Content();
		bean.setTitle("Testing integration");
		bean.setByline("More testing databases");
		bean.setBody("Meh");
		author.setContents(Arrays.asList(bean));
		author.setAddresses(Arrays.asList(new Address("ten_1 Rd")));
		role = new Role();
		role.setName(RoleName.ADMIN);

		Author authorForLambda2 = author;
		Role roleForLambda2 = role;
		Ebean.execute(() -> {
			roleForLambda2.save();
			authorForLambda2.setRoles(Arrays.asList(roleForLambda2));
			authorForLambda2.save();
		});

		UserContext.set("fi", "ten_2");

		Author OtherAuthor = new Author();
		OtherAuthor.setName("fi");
		Content beanOther = new Content();
		beanOther.setTitle("Banana");
		beanOther.setByline("Yummy and yellow");
		beanOther.setBody("Food content");
		OtherAuthor.setContents(Arrays.asList(beanOther));
		OtherAuthor.setAddresses(Arrays.asList(new Address("ten_2 Rd")));
		role = new Role();
		role.setName(RoleName.ADMIN);

		Author authorForLambda3 = OtherAuthor;
		Role roleForLambda3 = role;
		Ebean.execute(() -> {
			roleForLambda3.save();
			authorForLambda3.setRoles(Arrays.asList(roleForLambda3));
			authorForLambda3.save();
		});

		System.out.println("Tenant r1");
		// findAll1("ten_1");

		System.out.println("Tenant f1");
		// findAll1("ten_2");

		System.out.println("--------------------------next round----------------------------\n");

		System.out.println("Tenant r1");
		findAll2("ten_1");

		System.out.println("Tenant f1");
		// findAll2("ten_2");

		System.out.println("--------------------------next round----------------------------\n");

		System.out.println("Tenant r1");
		findAuthor(author.getId(), "ten_1");
		
		System.out.println("Tenant f1");
		findAuthor(OtherAuthor.getId(), "ten_2");
	}

	// runs well
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

	private void findAll2(String tenant) {

		CompletionStage<List<Content>> allForTenant = CompletableFuture.supplyAsync(() -> {
			UserContext.set("other", tenant);
			System.out.println("find all (for the current tenant) " + UserContext.get().getTenantId());
			return Ebean.find(Content.class).findList();
		});

		try {
			allForTenant.toCompletableFuture().get().forEach(content -> {
				System.out.println("Author: " + content.getAuthor().getName());
				System.out.println("Roles: " + content.getAuthor()
						.getRoles()
						.stream()
						.map(role -> role.getName().getValue())
						.collect(Collectors.toList()));
				System.out.println("Content: " + content);
			});
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	public void findAuthor(UUID id, String tenant) {

		CompletionStage<Author> authorFuture = CompletableFuture.supplyAsync(() -> {
			UserContext.set("other", tenant);
			return Ebean.find(Author.class).where().eq("id", id).findUnique();
		});

		Author author;
		try {
			author = authorFuture.toCompletableFuture().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}

		System.out.println("Author: " + author.getName() + "'s addresses: "
				+ author.getAddresses().stream().map(address -> address.getDescription()).collect(Collectors.toList()));
		System.out.println("Author: " + author.getName() + "'s roles: "
				+ author.getRoles().stream().map(role -> role.getName().getValue()).collect(Collectors.toList()));
		// CompletableFuture.runAsync(() -> System.out.println("Author: " +
		// author.getName() + "'s contents: "
		// + author.getRoles().stream().map(role ->
		// role.getName().getValue()).collect(Collectors.toList())));

	}
}