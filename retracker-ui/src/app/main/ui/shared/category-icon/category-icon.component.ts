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
  size = input<"large" | "small">("large");

  categorySingleLetter = computed(() => this.userCategory().categoryName ?? ''.length > 0 ? this.userCategory().categoryName[0].toUpperCase() : '?');
  
}
