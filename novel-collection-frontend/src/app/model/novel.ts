
export class Novel {
  id: number;
  slug: string;
  title: string;
  description: string;
  author: string;
  thumbnailURL: string;

  type: string; // Chinese, Korean....
  status: string; // Completed, Ongoing, Canceled, On Hold

  lastReleasedChapter: number;
  lastReleasedDate: string;

  source: string;
  rating: number;

  genres: Genre[];
  added: boolean;
  readmore: boolean = false;
}

export class Genre {
  id: number;
  genre: string;
}

export class Chapter {
  compositeKey: ChapterKey;
	title : string;
	content : string;
}

export class ChapterKey {
  novel: Novel
  number: number
}