import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import { NgClass } from '@angular/common';
import { UserCategory } from '../../../data/task.model';

@Component({
  selector: 'app-category-icon',
  imports: [NgClass],
  templateUrl: './category-icon.component.html',
  styleUrl: './category-icon.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CategoryIconComponent { 
  userCategory = input.required<UserCategory>();
  taskName = input.required<string>();
  size = input<"large" | "small">("large");

  categorySingleLetter = computed(() => this.taskName() ?? ''.length > 0 ? this.taskName()[0].toUpperCase() : '?');
  
}
