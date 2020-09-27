import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubredditNotFoundComponent } from './subreddit-not-found.component';

describe('SubredditNotFoundComponent', () => {
  let component: SubredditNotFoundComponent;
  let fixture: ComponentFixture<SubredditNotFoundComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubredditNotFoundComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubredditNotFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
