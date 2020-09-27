import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SubredditComponent } from '../../subreddit/subreddit.component';
import { SubredditNotFoundComponent } from '../../subreddit/subreddit-not-found/subreddit-not-found.component';

const routes : Routes = [
  {path: 'r/:subreddit_id', component: SubredditComponent},
  {path: '', redirectTo: 'r/all' , pathMatch: 'full'}
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class RoutingModule { }
