package com.challenge.supera.ManagementTask.service.imp;

import com.challenge.supera.ManagementTask.application.web.builder.ItemBuilder;
import com.challenge.supera.ManagementTask.application.web.dto.requesties.ItemRequest;
import com.challenge.supera.ManagementTask.application.web.dto.responses.ItemResponse;
import com.challenge.supera.ManagementTask.domain.model.Item;
import com.challenge.supera.ManagementTask.domain.model.Lista;
import com.challenge.supera.ManagementTask.repository.postgres.interfaces.ItemRepository;
import com.challenge.supera.ManagementTask.repository.postgres.interfaces.adapter.ItemAdapter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService implements com.challenge.supera.ManagementTask.service.interfaces.ItemService {

    private final ItemRepository repository;
    private final ItemBuilder builderItem;
    private final ItemAdapter adapter;

    public ItemService(ItemRepository repository, ItemBuilder builder, ItemAdapter adapter) {
        this.repository = repository;
        this.builderItem = builder;
        this.adapter = adapter;
    }

    @Override
    public ItemResponse create(ItemRequest request, Lista lista) {
        Item newItem = builderItem.toItemEntity(request, lista);
        Item savedItem = repository.save(newItem);

        return adapter.toResponse(savedItem);
    }

    @Override
    public List<ItemResponse> getItensByList(String listId) {
        List<Item> items = repository.findByListId(listId);
        return items.stream().map(adapter::toResponse).collect(Collectors.toList());
    }

    @Override
    public ItemResponse updatedItem(String id, ItemRequest request) {
        Item item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        adapter.updateItemFromRequest(item, request);
        repository.save(item);

        return adapter.toResponse(item);
    }

    @Override
    public void removeItem(String id) {
        repository.deleteById(id);
    }
}
