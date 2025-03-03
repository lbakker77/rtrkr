import {  computed, inject, Injectable, resource } from '@angular/core';
import { ListService } from './list.service';
import { firstValueFrom } from 'rxjs';
import { TaskService } from './task.service';

@Injectable({
  providedIn: 'root'
})
export class UserCategoryService {
  private taskService = inject(TaskService);

  constructor() { }

  private categoryResource = resource({
    loader: () => firstValueFrom(this.taskService.getUserCategories())
  });

  isReady = computed(() => this.categoryResource.hasValue());
  categories = computed(() => this.categoryResource.value());
}
